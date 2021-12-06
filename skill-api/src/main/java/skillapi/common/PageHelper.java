package skillapi.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Jun
 * @date 2020/10/20.
 */
public final class PageHelper<T> {
    private final List<T> data;
    private final int pageSize;

    private int pageNow;

    private List<T> cache;

    public PageHelper(Collection<T> pagingList, int size) {
        this.data = new ArrayList<>(pagingList);
        this.pageSize = size;
        this.pageNow = 1;

        // init cache
        getPage(this.pageNow);
    }

    public List<T> getCurrentPage() {
        return this.cache;
    }

    public List<T> getPage(int pageNum) {
        if (!rangeCheck(pageNum)) {
            return cache(Collections.emptyList());
        }

        int size = Math.min(pageNum * pageSize, this.data.size());
        if (size == 0) {
            return cache(Collections.emptyList());
        }

        return cache(this.data.subList((pageNum - 1) * pageSize, size));
    }

    /**
     * Avoid repeatedly creating multiple List objects in the rendering function.
     * e.g. {@link skillapi.client.gui.SkillConfigGui}
     *
     * @param cache Data to be cached
     * @return Cache object
     */
    private List<T> cache(List<T> cache) {
        this.cache = cache;
        return this.cache;
    }

    private void cachePageNow() {
        getPage(this.pageNow);
    }

    private boolean rangeCheck(int pageNum) {
        if (pageNum < 1) {
            return false;
        }
        return (pageNum - 1) * this.pageSize < this.data.size();
    }

    public void nextPage() {
        this.pageNow++;
        cachePageNow();
    }

    public void prevPage() {
        this.pageNow--;
        cachePageNow();
    }

    public List<T> getNextPage() {
        return getPage(++this.pageNow);
    }

    public List<T> getPrevPage() {
        return getPage(--this.pageNow);
    }

    public boolean hasNextPage() {
        return rangeCheck(this.pageNow + 1);
    }

    public boolean hasPrevPage() {
        return rangeCheck(this.pageNow - 1);
    }

    public void add(T object) {
        this.data.add(object);
    }

    public T remove(int index) {
        T o = this.data.remove(index);

        if ((this.data.size() + this.pageSize - 1) / this.pageSize >= this.pageNow) {
            cachePageNow();
        } else {
            prevPage();
        }
        return o;
    }

    public T removeInCurrentPage(int index) {
        return this.remove((this.pageNow - 1) * this.pageSize + index);
    }

    public List<T> toLastPage() {
        this.pageNow = (this.data.size() + this.pageSize - 1) / this.pageSize;
        return getPage(this.pageNow);
    }

    public List<T> addAndToLastPage(T object) {
        add(object);
        return toLastPage();
    }

    public int dataSize() {
        return this.data.size();
    }
}
