/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zuoxiaolong.niubi.job.core.config;

import com.zuoxiaolong.niubi.job.core.bean.DefaultJobBeanFactory;
import com.zuoxiaolong.niubi.job.core.bean.JobBeanFactory;
import com.zuoxiaolong.niubi.job.core.helper.ClassHelper;

/**
 * @author Xiaolong Zuo
 * @since 16/1/9 23:23
 */
public class DefaultContext implements Context {

    private JobScanClassLoader classLoader;

    private JobBeanFactory jobBeanFactory;

    private Configuration configuration;

    public DefaultContext() {
        this(ClassHelper.getDefaultClassLoader());
    }

    public DefaultContext(ClassLoader classLoader) {
        this(classLoader, new DefaultJobBeanFactory());
    }

    public DefaultContext(ClassLoader classLoader, JobBeanFactory jobBeanFactory) {
        this.classLoader = new JobScanClassLoader(classLoader);
        this.configuration = new Configuration(this.classLoader);
        this.jobBeanFactory = jobBeanFactory;
    }

    @Override
    public JobScanClassLoader classLoader() {
        return classLoader;
    }

    public JobBeanFactory jobBeanFactory() {
        return jobBeanFactory;
    }

    public Configuration configuration() {
        return configuration;
    }

}
