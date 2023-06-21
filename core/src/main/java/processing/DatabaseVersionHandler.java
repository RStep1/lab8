package processing;

import java.util.concurrent.atomic.AtomicLong;

public class DatabaseVersionHandler {
    private static final AtomicLong databaseVersion = new AtomicLong(0);

    public static long getDatabaseVersion() {
        return databaseVersion.get();
    }

    public static void setNewVersion() {
        databaseVersion.incrementAndGet();
    }

    public static synchronized boolean compareVersions(long userVersion) {
        return databaseVersion.get() == userVersion;
    }
}
