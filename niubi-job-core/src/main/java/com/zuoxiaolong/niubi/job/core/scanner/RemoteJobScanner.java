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

package com.zuoxiaolong.niubi.job.core.scanner;

import com.zuoxiaolong.niubi.job.core.config.Context;
import com.zuoxiaolong.niubi.job.core.helper.ClassHelper;
import com.zuoxiaolong.niubi.job.core.helper.IOHelper;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 默认的任务扫描器
 *
 * @author Xiaolong Zuo
 * @since 16/1/9 00:45
 */
public class RemoteJobScanner extends AbstractJobScanner {

    private String jarUrl;

    public RemoteJobScanner(Context context, String jarUrl) {
        super(context);
        this.jarUrl = jarUrl;
    }

    @Override
    public List<MethodTriggerDescriptor> scan() {
        List<MethodTriggerDescriptor> descriptorList = new ArrayList<MethodTriggerDescriptor>();
        try {
            String jarFilePath = downloadJarFile();
            getContext().classLoader().addURL(new URL("file:/" + jarFilePath));
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            while (jarEntryEnumeration.hasMoreElements()) {
                String jarEntryName = jarEntryEnumeration.nextElement().getName();
                if (jarEntryName == null || !jarEntryName.endsWith(".class")) {
                    continue;
                }
                String className = ClassHelper.getClassName(jarEntryName);
                super.scanClass(className, descriptorList);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return descriptorList;
    }

    private String downloadJarFile() throws IOException {
        String jarFileName = jarUrl.substring(jarUrl.lastIndexOf("/") + 1);
        String jarFilePath = getContext().classLoader().getResource("").getFile() + jarFileName;
        File file = new File(jarFilePath);
        if (file.exists()) {
            return jarFilePath;
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(jarUrl).openConnection();
        connection.connect();
        byte[] bytes = IOHelper.readStreamBytesAndClose(connection.getInputStream());
        IOHelper.writeFile(jarFilePath, bytes);
        return jarFilePath;
    }

}
