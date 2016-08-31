package nl.weeaboo.filesystem;

import nl.weeaboo.common.Checks;

public final class FileCollectOptions {

    public FilePath prefix;

    public boolean recursive;
    public boolean collectFiles;
    public boolean collectFolders;

    public FileCollectOptions() {
        this(FilePath.empty());
    }
    public FileCollectOptions(FilePath prefix) {
        this.prefix = Checks.checkNotNull(prefix);
    }

}
