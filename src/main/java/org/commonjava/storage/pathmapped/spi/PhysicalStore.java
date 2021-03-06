package org.commonjava.storage.pathmapped.spi;

import org.commonjava.storage.pathmapped.core.FileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface PhysicalStore
{
    FileInfo getFileInfo( String fileSystem, String path );

    OutputStream getOutputStream( FileInfo fileInfo ) throws IOException;

    InputStream getInputStream( String storageFile ) throws IOException;

    boolean delete( FileInfo fileInfo );
}
