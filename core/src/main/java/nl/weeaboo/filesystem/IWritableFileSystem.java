package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.OutputStream;

public interface IWritableFileSystem extends IFileSystem {

    void delete(String path) throws IOException;
    void rename(String src, String dst) throws IOException;
    void copy(String src, String dst) throws IOException;        
    
    OutputStream openOutputStream(String path, boolean append) throws IOException;
    
}
