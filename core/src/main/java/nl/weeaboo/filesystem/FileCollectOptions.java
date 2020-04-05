package nl.weeaboo.filesystem;

import nl.weeaboo.common.Checks;

public final class FileCollectOptions {

    private FilePath baseFolder = FilePath.empty();
    private String namePrefix = "";

    public boolean recursive;
    public boolean collectFiles;
    public boolean collectFolders;

    /**
     * A recursive, file-only filter.
     */
    public FileCollectOptions() {
        recursive = true;
        collectFiles = true;
    }

    /**
     * A recursive, folder-only filter that only accepts paths starting with the given prefix.
     */
    public static FileCollectOptions subFolders(FilePath basePath) {
        FileCollectOptions opts = new FileCollectOptions();
        opts.setBaseFolder(basePath);
        opts.collectFiles = false;
        opts.collectFolders = true;
        return opts;
    }

    /**
     * @see #files(FilePath, String)
     */
    public static FileCollectOptions files(FilePath basePath) {
        return files(basePath, "");
    }

    /**
     * A recursive, folder-only filter that only accepts paths starting with the given prefix.
     */
    public static FileCollectOptions files(FilePath basePath, String namePrefix) {
        FileCollectOptions opts = new FileCollectOptions();
        opts.setBaseFolder(basePath);
        opts.setNamePrefix(namePrefix);
        opts.collectFiles = true;
        opts.collectFolders = false;
        return opts;
    }

    /**
     * Checks if the supplied path passes the filter.
     */
    public boolean isValid(FilePath file) {
        FilePath parent = file.getParent();
        if (parent == null) {
            parent = FilePath.empty();
        }

        if (file.equals(baseFolder)) {
            return false; // Don't include the base folder
        }

        if (recursive) {
            // This folder is equal to, or a descendant of the base search path
            return file.getName().startsWith(namePrefix) && parent.startsWith(baseFolder);
        } else {
            // This file is a direct child of the base search path
            return file.getName().startsWith(namePrefix) && parent.equals(baseFolder);
        }
    }

    public FilePath getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(FilePath baseFolder) {
        this.baseFolder = Checks.checkNotNull(baseFolder);
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = Checks.checkNotNull(namePrefix);
    }

}
