package processing;

import java.util.concurrent.atomic.AtomicLong;

public class DatabaseVersionHandler {
    private static final AtomicLong DATABASE_VERSION = new AtomicLong(0);

    public static long getDatabaseVersion() {
        return DATABASE_VERSION.get();
    }

    public static synchronized void setNewVersion() {
        DATABASE_VERSION.incrementAndGet();
        if (DATABASE_VERSION.get() == Long.MAX_VALUE) {
            DATABASE_VERSION.set(0);
        }
    }

    public static synchronized boolean compareVersions(long userVersion) {
        return DATABASE_VERSION.get() == userVersion;
    }
}
