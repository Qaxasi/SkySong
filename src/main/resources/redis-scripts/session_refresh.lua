
-- KEYS[1] = oldKey
-- KEYS[2] = newKey
-- KEYS[3] = setKey
-- ARGV[1] = payload - json
-- ARGV[2] = ttlSeconds
-- ARGV[3] = oldHash
-- ARGV[4] = newHash

local RES_OK = 1
local RES_NOT_FOUND = 0
local RES_CONFLICT = 2

if (#KEYS ~= 3 or #ARGV ~= 4) then
    return redis.error_reply("wrong arity")
end

local oldKey = KEYS[1]
local newKey = KEYS[2]
local setKey = KEYS[3]
local payload = ARGV[1]
local ttl = tonumber(ARGV[2]) or 0
local oldHash = ARGV[3]
local newHash = ARGV[4]

ttl = math.floor(ttl)
if ttl <= 0 then
    return redis.error_reply("bad ttl")
end

if oldKey == newKey then
    if redis.call('EXISTS', oldKey) == 0 then
        return RES_NOT_FOUND
    end

    redis.call('SET', oldKey, payload, 'EX', ttl)

    if oldHash ~= newHash then
        redis.call("SREM", setKey, oldHash)
    end
    redis.call("SADD", setKey, newHash)

else
    if redis.call('EXISTS', oldKey) == 0 then
        return RES_NOT_FOUND
    end


    if not redis.call('SET', newKey, payload, 'EX', ttl, "NX") then
        return RES_CONFLICT
    end


    redis.call('UNLINK', oldKey)

    if oldHash ~= newHash then
        redis.call('SREM', setKey, oldHash)
    end
    redis.call('SADD', setKey, newHash)
end


local t = redis.call('TTL', setKey)
if t == -1 or t < ttl then
    redis.call('EXPIRE', setKey, ttl)
end

return RES_OK

