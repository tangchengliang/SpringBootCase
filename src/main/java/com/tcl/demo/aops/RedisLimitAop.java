package com.tcl.demo.aops;

import com.tcl.demo.annotations.RedisLimitAnnotation;
import com.tcl.demo.exception.RedisLimitException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Aspect
@Component
public class RedisLimitAop
{
    Object result = null;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    private DefaultRedisScript<Long> redisLuaScript;
    @PostConstruct
    public void init()
    {
        redisLuaScript = new DefaultRedisScript<>();
        redisLuaScript.setResultType(Long.class);
        redisLuaScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimiter.lua")));
    }

    @Around("@annotation(com.tcl.demo.annotations.RedisLimitAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint)
    {
        System.out.println("---------环绕通知1111111");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //拿到RedisLimitAnnotation注解，如果存在则说明需要限流，容器捞鱼思想
        RedisLimitAnnotation redisLimitAnnotation = method.getAnnotation(RedisLimitAnnotation.class);

        if (redisLimitAnnotation != null)
        {
            //获取redis的key
            String key = redisLimitAnnotation.key();
            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();

            String limitKey = key +"\t"+ className+"\t" + methodName;
            log.info(limitKey);

            if (null == key)
            {
                throw new RedisLimitException("it's danger,limitKey cannot be null");
            }

            long limit = redisLimitAnnotation.permitsPerSecond();
            long expire = redisLimitAnnotation.expire();
            List<String> keys = new ArrayList<>();
            keys.add(key);

            Long count = stringRedisTemplate.execute(
                    redisLuaScript,
                    keys,
                    String.valueOf(limit),
                    String.valueOf(expire));

            System.out.println("Access try count is "+count+" \t key= "+key);
            if (count != null && count == 0)
            {
                System.out.println("启动限流功能key: "+key);
                //throw new RedisLimitException(redisLimitAnnotation.msg());
                return redisLimitAnnotation.msg();
            }
        }


        try {
            result = joinPoint.proceed();//放行
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        System.out.println("---------环绕通知2222222");
        System.out.println();
        System.out.println();

        return result;
    }
}
