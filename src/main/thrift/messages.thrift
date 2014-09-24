namespace java com.dbaneman.leveldb.internal

struct GetResult {
    1: required bool exists
    2: optional binary value
}

struct KeyValPair {
    1: required binary key
    2: required binary value
}

struct Range {
    1: required binary start
    2: required binary limit
}

service DbService {
    GetResult get(1:binary key),
    void doDelete(1:binary key),
    void put(1:binary key, 2:binary value),
    list<i64> getApproximateSizes(1:list<Range> ranges),
    string getProperty(1:string property),
    void suspendCompactions(),
    void resumeCompactions(),
    void compactRange(1: binary start; 2: binary limit),
    string iterator(),
    string writeBatch(),
    void write(1: string id)

    bool iterHasNext(1: string id),
    KeyValPair iterPeekNext(1: string id),
    KeyValPair iterNext(1: string id),
    bool iterHasPrev(1: string id),
    KeyValPair iterPeekPrev(1: string id),
    KeyValPair iterPrev(1: string id),
    void iterSeek(1: string id, 2:binary key),
    void iterSeekToFirst(1: string id),
    void iterSeekToLast(1: string id),
    void iterClose(1: string id),

    void wbPut(1: string id, 2:binary key, 3: binary value),
    void wbDelete(1: string id, 2:binary key),
    void wbClose(1: string id)
}