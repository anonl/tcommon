package nl.weeaboo.filesystem;

import java.io.Serializable;

import nl.weeaboo.io.Filenames;

public final class FilePath implements Serializable, Comparable<FilePath> {

    private static final long serialVersionUID = 1L;

    private static final FilePath EMPTY = new FilePath("");

    private final String path;

    private FilePath(String path) {
        this.path = path;
    }

    public static FilePath empty() {
        return EMPTY;
    }

    public static FilePath of(String pathString) {
        return new FilePath(normalize(pathString));
    }

    private static String normalize(String pathString) {
        // Replace runs of multiple '/' characters
        // Replace '\\' with '/'
        if (pathString.contains("//") || pathString.indexOf('\\') >= 0) {
            pathString = pathString.replaceAll("[\\/]+", "/");
        }

        // Strip leading '/'
        if (pathString.startsWith("/")) {
            pathString = pathString.substring(1);
        }

        return pathString;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FilePath)) {
            return false;
        }

        FilePath other = (FilePath)obj;
        return path.equals(other.path);
    }

    @Override
    public int compareTo(FilePath other) {
        return path.compareTo(other.path);
    }

    @Override
    public String toString() {
        return path;
    }

    public FilePath resolve(FilePath relPath) {
        return FilePath.of(path + "/" + relPath);
    }
    public FilePath resolve(String relPath) {
        return FilePath.of(path + "/" + relPath);
    }

    public FilePath relativize(FilePath fullPath) {
        String fullPathString = fullPath.path;
        if (fullPathString.startsWith(path)) {
            return FilePath.of(fullPathString.substring(path.length()));
        } else {
            // Not a relative path
            return fullPath;
        }
    }

    public boolean isFolder() {
        return path.endsWith("/");
    }

    public FilePath getParent() {
        int index = getSplitIndex();
        if (index < 0) {
            return null;
        }
        return new FilePath(path.substring(0, index + 1));
    }

    public String getName() {
        int index = getSplitIndex();
        return path.substring(index + 1);
    }

    public String getExt() {
        return Filenames.getExtension(getName());
    }

    public FilePath withExt(String newExtension) {
        return FilePath.of(Filenames.replaceExt(path, newExtension));
    }

    public boolean hasParent() {
        return getSplitIndex() >= 0;
    }

    private int getSplitIndex() {
        if (isFolder()) {
            return path.lastIndexOf('/', path.length() - 1);
        } else {
            return path.lastIndexOf('/');
        }
    }

    public boolean startsWith(FilePath prefix) {
        return path.startsWith(prefix.path);
    }

    public boolean endsWith(FilePath prefix) {
        return path.endsWith(prefix.path);
    }

}
