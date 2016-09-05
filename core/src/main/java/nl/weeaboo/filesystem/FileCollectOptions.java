package nl.weeaboo.filesystem;

import nl.weeaboo.common.Checks;

public final class FileCollectOptions {

    public final FilePath prefix;

    public boolean recursive = true;
    public boolean collectFiles = true;
    public boolean collectFolders;

    public FileCollectOptions() {
        this(FilePath.empty());
    }

    public FileCollectOptions(FilePath prefix) {
        this.prefix = Checks.checkNotNull(prefix);
    }

    public static FileCollectOptions folders(FilePath basePath) {
        FileCollectOptions opts = new FileCollectOptions(basePath);
        opts.collectFiles = false;
        opts.collectFolders = true;
        return opts;
    }

    public static FileCollectOptions files(FilePath basePath) {
        FileCollectOptions opts = new FileCollectOptions(basePath);
        opts.collectFiles = true;
        opts.collectFolders = false;
        return opts;
    }

}
