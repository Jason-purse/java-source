/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package sun.security.util;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;

/**
 * This {@code PermissionCollection} implementation delegates to another
 * {@code PermissionCollection}, taking care to lazily add the permission needed
 * to read from the given {@code CodeSource} at first use, i.e., when either of
 * {@link #elements}, {@link #implies} or {@link #toString} is called, or when
 * the collection is serialized.
 */
public final class LazyCodeSourcePermissionCollection
        extends PermissionCollection
{
    private static final long serialVersionUID = -6727011328946861783L;
    private final PermissionCollection perms;
    private final CodeSource cs;
    private volatile boolean permissionAdded;

    public LazyCodeSourcePermissionCollection(PermissionCollection perms,
                                              CodeSource cs) {
        this.perms = perms;
        this.cs = cs;
    }

    private void ensureAdded() {
        if (!permissionAdded) {
            synchronized(perms) {
                if (permissionAdded)
                    return;

                // open connection to determine the permission needed
                URL location = cs.getLocation();
                if (location != null) {
                    try {
                        Permission p = location.openConnection().getPermission();
                        if (p != null) {
                            // for directories then need recursive access
                            if (p instanceof FilePermission) {
                                String path = p.getName();
                                if (path.endsWith(File.separator)) {
                                    path += "-";
                                    p = new FilePermission(path,
                                            SecurityConstants.FILE_READ_ACTION);
                                }
                            }
                            perms.add(p);
                        }
                    } catch (IOException ioe) {
                    }
                }
                if (isReadOnly()) {
                    perms.setReadOnly();
                }
                permissionAdded = true;
            }
        }
    }

    @Override
    public void add(Permission permission) {
        if (isReadOnly())
            throw new SecurityException(
                    "attempt to add a Permission to a readonly PermissionCollection");
        perms.add(permission);
    }

    @Override
    public boolean implies(Permission permission) {
        ensureAdded();
        return perms.implies(permission);
    }

    @Override
    public Enumeration<Permission> elements() {
        ensureAdded();
        return perms.elements();
    }

    @Override
    public String toString() {
        ensureAdded();
        return perms.toString();
    }

    /**
     * On serialization, initialize and replace with the underlying
     * permissions. This removes the laziness on deserialization.
     */
    private Object writeReplace() {
        ensureAdded();
        return perms;
    }
}
