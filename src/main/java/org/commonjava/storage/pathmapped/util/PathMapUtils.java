package org.commonjava.storage.pathmapped.util;

import org.commonjava.storage.pathmapped.model.PathMap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

public class PathMapUtils
{
    public final static String ROOT_DIR = "/";

    // looks weird but it splits "/path/to/my/file" -> [/][path/][to/][my/][file]
    private final static String EMPTY_CHAR_AFTER_SLASH = "(?<=/)";

    public static String getParentPath( String path )
    {
        if ( ROOT_DIR.equals( path ) )
        {
            return null; // root have no parent path
        }

        StringBuilder sb = new StringBuilder();
        String[] toks = path.split( EMPTY_CHAR_AFTER_SLASH );
        for ( int i = 0; i < toks.length - 1; i++ )
        {
            sb.append( toks[i] );
        }
        String ret = sb.toString();
        if ( ret.endsWith( "/" ) )
        {
            ret = ret.substring( 0, ret.length() - 1 ); // remove trailing /
        }
        if ( ret.length() <= 0 )
        {
            ret = ROOT_DIR;
        }
        return ret;
    }

    public static String getFilename( String path )
    {
        if ( ROOT_DIR.equals( path ) )
        {
            return null;
        }
        String[] toks = path.split( EMPTY_CHAR_AFTER_SLASH );
        return toks[toks.length - 1];
    }

    public static String getStoragePathByFileId( String id )
    {
        String folder = id.substring( 0, 2 );
        String subFolder = id.substring( 2, 4 );
        String filename = id.substring( 4 );
        return folder + "/" + subFolder + "/" + filename;
    }

    /**
     * Get parents starting in top-down order.
     */
    public static <R> List<R> getParents( PathMap pathMap,
                                             PathMapCreation<String, String, String, R> pathMapCreation )
    {
        String fileSystem = pathMap.getFileSystem();
        String parent = pathMap.getParentPath(); // e.g, /foo/bar/1.0

        LinkedList<R> l = new LinkedList<>();

        String parentPath = ROOT_DIR;

        String[] toks = parent.split( "/" ); // [][foo][bar][1.0]
        for ( String tok : toks )
        {
            if ( isBlank( tok ) )
            {
                continue;
            }
            String filename = tok + "/";
            R o = pathMapCreation.apply( fileSystem, parentPath, filename );
            l.add( o );
            if ( !parentPath.endsWith( "/" ) )
            {
                parentPath += "/";
            }
            parentPath += tok;
        }
        return l;
    }

    @FunctionalInterface
    public interface PathMapCreation<T1, T2, T3, R>
    {
        R apply( T1 fileSystem, T2 parentPath, T3 filename );
    }

    public static <R> List<R> getParentsBottomUp( PathMap pathMap,
                                                    PathMapCreation<String, String, String, R> pathMapCreation )
    {
        List<R> l = getParents( pathMap, pathMapCreation );
        Collections.reverse( l );
        return l;
    }

    public static String marshall( String fileSystem, String path )
    {
        return fileSystem + ":" + path;
    }

    public static String normalize( final String... path )
    {
        if ( path == null || path.length < 1 || ( path.length == 1 && path[0] == null ) )
        {
            return ROOT_DIR;
        }

        final StringBuilder sb = new StringBuilder();
        int idx = 0;
        parts:
        for ( String part : path )
        {
            if ( part == null || part.length() < 1 || "/".equals( part ) )
            {
                continue;
            }
            if ( idx == 0 && part.startsWith( "file:" ) )
            {
                if ( part.length() > 5 )
                {
                    sb.append( part.substring( 5 ) );
                }
                continue;
            }
            if ( idx > 0 )
            {
                while ( part.charAt( 0 ) == '/' )
                {
                    if ( part.length() < 2 )
                    {
                        continue parts;
                    }
                    part = part.substring( 1 );
                }
            }
            while ( part.charAt( part.length() - 1 ) == '/' )
            {
                if ( part.length() < 2 )
                {
                    continue parts;
                }
                part = part.substring( 0, part.length() - 1 );
            }
            if ( sb.length() > 0 )
            {
                sb.append( '/' );
            }
            sb.append( part );
            idx++;
        }

        if ( path[path.length - 1] != null && path[path.length - 1].endsWith( "/" ) )
        {
            sb.append( "/" );
        }
        return sb.toString();
    }
}
