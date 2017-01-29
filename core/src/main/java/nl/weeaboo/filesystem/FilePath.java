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

    /**
     * Returns an empty path instance.
     */
    public static FilePath empty() {
        return EMPTY;
    }

    /**
     * Converts the path string to an equivalent {@code FilePath} instance.
     */
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

    /**
     * Constructs a new path using a subpath relative to the folder represented by this path.
     */
    public FilePath resolve(FilePath relPath) {
        return FilePath.of(path + "/" + relPath);
    }

    /**
     * Constructs a new path using a subpath relative to the folder represented by this path.
     *
     * @see #resolve(FilePath)
     */
    public FilePath resolve(String relPath) {
        return FilePath.of(path + "/" + relPath);
    }

    /**
     * If the supplied path is a subpath of the folder represented by this path, returns a new path relative to this
     * folder. If unable to resolve as a relative path, the supplied path is returned unchanged.
     */
    public FilePath relativize(FilePath fullPath) {
        String fullPathString = fullPath.path;
        if (fullPathString.startsWith(path)) {
            return FilePath.of(fullPathString.substring(path.length()));
        } else {
            // Not a relative path
            return fullPath;
        }
    }

    /**
     * Returns {@code true} if this path represents a folder.
     */
    public boolean isFolder() {
        return path.length() == 0 || path.endsWith("/");
    }

    /**
     * Returns this paths parent folder, or {@code null} if this path has no parent.
     */
    public FilePath getParent() {
        int index = getSplitIndex();
        if (index < 0) {
            return null;
        }
        return new FilePath(path.substring(0, index + 1));
    }

    /**
     * Returns the final (file name) part of this path.
     */
    public String getName() {
        int index = getSplitIndex();
        return path.substring(index + 1);
    }

    /**
     * Returns the file extension of this path's file name.
     *
     * @return The file extension, or {@code ""} if this path doesn't have a file extension.
     */
    public String getExt() {
        return Filenames.getExtension(getName());
    }

    /**
     * Returns a new path with a modified file extension.
     */
    public FilePath withExt(String newExtension) {
        return FilePath.of(Filenames.replaceExt(path, newExtension));
    }

    /**
     * Returns {@code true} if this path has a parent folder.
     */
    public boolean hasParent() {
        return getSplitIndex() >= 0;
    }

    private int getSplitIndex() {
        if (isFolder()) {
            return path.lastIndexOf('/', path.length() - 2);
        } else {
            return path.lastIndexOf('/');
        }
    }

    /**
     * Checks if this path starts with the given prefix.
     */
    public boolean startsWith(FilePath prefix) {
        return path.startsWith(prefix.path);
    }

    /**
     * Checks if this path ends with the given prefix.
     */
    public boolean endsWith(FilePath prefix) {
        return path.endsWith(prefix.path);
    }

}
