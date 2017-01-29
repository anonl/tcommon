package nl.weeaboo.filesystem;

import nl.weeaboo.common.Checks;

public final class FileCollectOptions {

    private FilePath prefix;

    public boolean recursive;
    public boolean collectFiles;
    public boolean collectFolders;

    /**
     * A recursive, file-only filter.
     */
    public FileCollectOptions() {
        prefix = FilePath.empty();
        recursive = true;
        collectFiles = true;
    }

    /**
     * A recursive, folder-only filter that only accepts paths starting with the given prefix.
     */
    public static FileCollectOptions folders(FilePath basePath) {
        FileCollectOptions opts = new FileCollectOptions();
        opts.setPrefix(basePath);
        opts.collectFiles = false;
        opts.collectFolders = true;
        return opts;
    }

    /**
     * A recursive, folder-only filter that only accepts paths starting with the given prefix.
     */
    public static FileCollectOptions files(FilePath basePath) {
        FileCollectOptions opts = new FileCollectOptions();
        opts.setPrefix(basePath);
        opts.collectFiles = true;
        opts.collectFolders = false;
        return opts;
    }

    /**
     * Checks if the supplied path passes the filter.
     */
    public boolean isValid(FilePath file) {
        if (recursive) {
            // This folder is equal to, or a descendant of the base search path
            return file.startsWith(prefix);
        } else {
            // This file is equal to, or a direct child of the base search path
            if (prefix.equals(file)) {
                return true;
            }

            FilePath parent = file.getParent();
            if (parent == null) {
                return prefix.equals(FilePath.empty());
            } else {
                return prefix.equals(parent);
            }
        }
    }

    /**
     * Returns the required path prefix.
     */
    public FilePath getPrefix() {
        return prefix;
    }

    /**
     * Sets the required path prefix.
     */
    public void setPrefix(FilePath path) {
        prefix = Checks.checkNotNull(path);
    }

}
