namespace java com.dbaneman.leveldb.internal

struct GetResult {
    1: required bool exists
    2: optional binary value
}

service DbService {

    GetResult get(1:binary key),

    void deleteKey(1:binary key),

    void put(1:binary key, 2:binary value)

}