package rvmonitorrt.map;

import rvmonitorrt.ref.MOPMultiTagWeakReference;
import rvmonitorrt.ref.MOPTagWeakReference;
import rvmonitorrt.ref.MOPWeakReference;

public interface MOPRefMap {
	static final int ref_cleanup_piece = 16;
	static final int ref_locality_cache_size = 32;
	
	public MOPWeakReference getRef(Object key, int joinPointId);

	public MOPWeakReference getRefNonCreative(Object key, int joinPointId);

	public MOPTagWeakReference getTagRef(Object key, int joinPointId);

	public MOPTagWeakReference getTagRefNonCreative(Object key, int joinPointId);
	
	public MOPMultiTagWeakReference getMultiTagRef(Object key, int joinPointId);

	public MOPMultiTagWeakReference getMultiTagRefNonCreative(Object key, int joinPointId);


	public MOPWeakReference getRef(Object key);

	public MOPWeakReference getRefNonCreative(Object key);

	public MOPTagWeakReference getTagRef(Object key);

	public MOPTagWeakReference getTagRefNonCreative(Object key);
	
	public MOPMultiTagWeakReference getMultiTagRef(Object key);

	public MOPMultiTagWeakReference getMultiTagRefNonCreative(Object key);
	
}
