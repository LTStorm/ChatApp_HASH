/*
 * Copyright (C) 2010 Teleal GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.teleal.common.http;

import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * @author Christian Bauer
 */
public class Representation<E> implements Serializable {

    private URL url;

    private CacheControl cacheControl;
    private Integer contentLength;
    private String contentType;
    private Long lastModified;
    private String entityTag;
    private E entity;

    public Representation(CacheControl cacheControl, Integer contentLength, String contentType, Long lastModified, String entityTag, E entity) {
        this(null, cacheControl, contentLength, contentType, lastModified, entityTag, entity);
    }

    public Representation(URL url, CacheControl cacheControl, Integer contentLength, String contentType, Long lastModified, String entityTag, E entity) {
        this.url = url;
        this.cacheControl = cacheControl;
        this.contentLength = contentLength;
        this.contentType = contentType;
        this.lastModified = lastModified;
        this.entityTag = entityTag;
        this.entity = entity;
    }

    public Representation(URLConnection urlConnection, E entity) {
        this(
                urlConnection.getURL(),
                CacheControl.valueOf(urlConnection.getHeaderField("Cache-Control")),
                urlConnection.getContentLength(),
                urlConnection.getContentType(),
                urlConnection.getLastModified(),
                urlConnection.getHeaderField("Etag"),
                entity
        );
    }

    public URL getUrl() {
        return url;
    }

    public CacheControl getCacheControl() {
        return cacheControl;
    }

    public Integer getContentLength() {
        return contentLength == null || contentLength == -1 ? null : contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getLastModified() {
        return lastModified == 0 ? null : lastModified;
    }

    public String getEntityTag() {
        return entityTag;
    }

    public E getEntity() {
        return entity;
    }

    public Long getMaxAgeOrNull() {
        return (getCacheControl() == null ||
                getCacheControl().getMaxAge() == -1 ||
                getCacheControl().getMaxAge() == 0)
                ? null
                : (long)getCacheControl().getMaxAge();
    }

    public boolean isExpired(long storedOn, long maxAge) {
        return (storedOn + (maxAge * 1000)) < new Date().getTime();
    }

    public boolean isExpired(long storedOn) {
        return getMaxAgeOrNull() == null || isExpired(storedOn, getMaxAgeOrNull());
    }

    public boolean isNoStore() {
        return getCacheControl() != null &&
                getCacheControl().isNoStore();
    }

    public boolean isNoCache() {
        return getCacheControl() != null &&
                getCacheControl().isNoCache();
    }

    public boolean mustRevalidate() {
        return getCacheControl() != null &&
                getCacheControl().isProxyRevalidate();
    }

    public boolean hasEntityTagChanged(String currentEtag) {
        return getEntityTag() != null
                && !getEntityTag().equals(currentEtag);
    }

    public boolean hasBeenModified(long currentModificationTime) {
        return getLastModified() == null || getLastModified() < currentModificationTime;
    }

    @Override
    public String toString() {
        return "(" + getClass().getSimpleName() + ") CT: " + getContentType();
    }

}
