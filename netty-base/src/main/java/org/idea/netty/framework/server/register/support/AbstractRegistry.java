package org.idea.netty.framework.server.register.support;

import org.idea.netty.framework.server.common.Node;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.common.utils.ConcurrentHashSet;
import org.idea.netty.framework.server.register.RegistryService;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegister;
import org.idea.netty.framework.server.util.PropertiesUtils;

import java.io.*;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.*;

import static org.idea.netty.framework.server.common.ConfigPropertiesKey.LOCAL_URL_STORE_LOCATION;

/**
 * 抽象注册服务类
 *
 * @Author linhao
 * @Date created in 12:24 下午 2021/1/6
 */
public abstract class AbstractRegistry implements Node, RegistryService {

    /**
     * 注册的url信息
     */
    private Set<URL> registryURLSet = new ConcurrentHashSet<>();
    private URL currentUrl;
    private File registryConfigFile;
    private boolean syncSaveFile = false;
    private boolean saveUrlInDisk = false;
    private Properties properties = new Properties();
    private ExecutorService syncSaveThreadPool = new ThreadPoolExecutor(1, 1,
            3L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(600), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "syncSaveFile_pool_" + r.hashCode());
        }
    }, new ThreadPoolExecutor.AbortPolicy());

    @Override
    public URL getUrl() {
        return currentUrl;
    }

    public void setUrl(URL currentUrl) {
        this.currentUrl = currentUrl;
    }


    /**
     * 构建本地url存储持久化的路径
     *
     * @param url
     * @param prefix
     * @return
     */
    private String buildLocalUrlStorePath(URL url, String prefix) {
        return prefix + "_" + url.getApplicationName().toUpperCase();
    }

    /**
     * 会将注册文件写入到一个流里面，然后持久化写入一份文件当中
     *
     * @param url
     */
    public AbstractRegistry(URL url) {
        this.currentUrl = url;
        this.syncSaveFile = (boolean) url.getParameter("syncSaveFile", false);
        this.saveUrlInDisk = (boolean) url.getParameter("saveUrlInDisk", true);
        if (!saveUrlInDisk) {
            System.out.println("不需要将url持久化到数据库");
            return;
        }
        String pathUrl = System.getProperty("user.home");
        String saveFilePath = buildLocalUrlStorePath(url, pathUrl);
        if (registryConfigFile == null) {
            registryConfigFile = new File(saveFilePath);
            try {
                registryConfigFile.createNewFile();
            } catch (IOException e) {
                //可能是没有权限写入数据
                try {
                    registryConfigFile = new File(buildLocalUrlStorePath(url, PropertiesUtils.getPropertiesStr(LOCAL_URL_STORE_LOCATION)));
                    registryConfigFile.createNewFile();
                } catch (Exception exp) {
                    throw new RuntimeException(exp);
                }
            }
        }
        if (registryConfigFile.exists()) {
            try {
                InputStream in = new FileInputStream(registryConfigFile);
                properties.load(in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void register(URL url) {
        registryURLSet.add(url);
        if (syncSaveFile) {
            this.doSaveCacheInDisk(url);
        } else {
            syncSaveThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    doSaveCacheInDisk(url);
                }
            });
        }
    }

    /**
     * 保存缓存持久化到磁盘中 这个操作应该设计在父类中，然后子类实现不同的doRegistry方法，但是细节点都会有共同点是用于实现持久化的步骤
     *
     * @param url
     */
    private void doSaveCacheInDisk(URL url) {
        properties.setProperty(url.getPath(), URL.buildUrlStr(url));
        try (FileOutputStream outputStream = new FileOutputStream(registryConfigFile)) {
            properties.store(outputStream, "Ietty Registry Cache");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unRegister(URL url) {
        registryURLSet.remove(url);
    }

    public Set<URL> getRegistryURLSet() {
        return registryURLSet;
    }

}
