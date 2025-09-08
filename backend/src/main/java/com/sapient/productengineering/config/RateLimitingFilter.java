package com.sapient.productengineering.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class RateLimitingFilter extends OncePerRequestFilter{

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String p=request.getRequestURI();
        return p.startsWith("/swagger")
                || p.startsWith("/v3")
                || p.startsWith("/h2-console")
                || p.startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)
            throws ServletException,IOException{
        String key=getClientKey(req);
        Bucket bucket=buckets.computeIfAbsent(key,k->newBucket());
        if (bucket.tryConsume(1)){
            chain.doFilter(req,res);
        }else{
            res.setStatus(429);
            res.getWriter().write("Too many requests");
        }
    }

    private Bucket newBucket(){
        Bandwidth limit=Bandwidth.classic(60,Refill.greedy(60,Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    private String getClientKey(HttpServletRequest req){
        String xf=req.getHeader("X-Forwarded-For");
        if (xf==null||xf.isBlank()){
            return req.getRemoteAddr();
        }
        String[] parts=xf.split(",");
        String first=parts.length>0?parts[0]:xf;
        return first.trim();
    }
}
