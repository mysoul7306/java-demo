/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 01
 * All Rights Reserved
 */

package kr.co.rokroot.demo.stream.service.framework.config;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@Slf4j
@EnableAsync
public class AsyncConfig {

    @Bean(name = "consumerThreadPool")
    public Executor consumerThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1);
        executor.setThreadNamePrefix("consumer-");
        executor.initialize();

        return HandlingExecutor.create(executor);
    }

    @Bean(name = "producerThreadPool")
    public Executor producerThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1);
        executor.setThreadNamePrefix("producer-");
        executor.initialize();

        return HandlingExecutor.create(executor);
    }

    @RequiredArgsConstructor
    public static class HandlingExecutor implements AsyncTaskExecutor {

        private final AsyncTaskExecutor executor;

        public static HandlingExecutor create(AsyncTaskExecutor executor) {
            return new HandlingExecutor(executor);
        }

        @Override
        public void execute(Runnable task) {
            this.executor.execute(runnableWrapper(task));
        }

        @Override
        public Future<?> submit(Runnable task) {
            return this.executor.submit(runnableWrapper(task));
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return this.executor.submit(runnableWrapper(task));
        }

        @Override
        public CompletableFuture<Void> submitCompletable(Runnable task) {
            return this.executor.submitCompletable(runnableWrapper(task));
        }

        @Override
        public <T> CompletableFuture<T> submitCompletable(Callable<T> task) {
            return this.executor.submitCompletable(runnableWrapper(task));
        }

        @PreDestroy
        public void destroy() {
            if (executor instanceof ThreadPoolTaskExecutor) {
                ((ThreadPoolTaskExecutor) executor).shutdown();
            }
        }


        private <T> Callable<T> runnableWrapper(final Callable<T> task) {
            return (Callable<T>) () -> {
                try {
                    return task.call();
                } catch (Exception e) {
                    handle(e);
                    throw e;
                }
            };
        }

        private Runnable runnableWrapper(final Runnable task) {
            return (Runnable) () -> {
                try {
                    task.run();
                } catch (Exception e) {
                    handle(e);
                }
            };
        }

        public void handle(Exception e) {
            log.error("| !!! Execute task Failed !!! |", e);
        }
    }
}
