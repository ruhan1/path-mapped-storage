package org.commonjava.storage.pathmapped.jpa.model;

import org.commonjava.storage.pathmapped.model.PathMap;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

@Entity
@Table( name = "pathmap" )
public class JpaPathMap implements PathMap
{
    @EmbeddedId
    private JpaPathKey pathKey;

    @Column( name = "fileid" )
    private String fileId;

    @Column
    private Date creation;

    @Column
    private int size;

    @Column( name = "filestorage" )
    private String fileStorage;

    public JpaPathMap()
    {
    }

    public JpaPathMap( JpaPathKey pathKey, String fileId, Date creation, int size, String fileStorage )
    {
        this.pathKey = pathKey;
        this.fileId = fileId;
        this.creation = creation;
        this.size = size;
        this.fileStorage = fileStorage;
    }

    public JpaPathKey getPathKey()
    {
        return pathKey;
    }

    public void setPathKey( JpaPathKey pathKey )
    {
        this.pathKey = pathKey;
    }

    @Override
    public String getFileSystem()
    {
        return pathKey.getFileSystem();
    }

    @Override
    public String getParentPath()
    {
        return pathKey.getParentPath();
    }

    @Override
    public String getFilename()
    {
        return pathKey.getFilename();
    }

    public String getFileId()
    {
        return fileId;
    }

    public void setFileId( String fileId )
    {
        this.fileId = fileId;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize( int size )
    {
        this.size = size;
    }

    public String getFileStorage()
    {
        return fileStorage;
    }

    public void setFileStorage( String fileStorage )
    {
        this.fileStorage = fileStorage;
    }

    public Date getCreation()
    {
        return creation;
    }

    public void setCreation( Date creation )
    {
        this.creation = creation;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        JpaPathMap pathMap = (JpaPathMap) o;
        return pathKey.equals( pathMap.pathKey );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( pathKey );
    }

    @Override
    public String toString()
    {
        return "PathMap{" + "pathKey=" + pathKey + ", fileId='" + fileId + '\'' + ", creation=" + creation + ", size="
                        + size + ", fileStorage='" + fileStorage + '\'' + '}';
    }
}
