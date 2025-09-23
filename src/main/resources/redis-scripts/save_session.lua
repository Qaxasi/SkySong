
-- KEYS[1] = mainKey
-- KEYS[2] = hashesSetKey
-- ARGV[1] = payload json
-- ARGV[2] = ttlSeconds
-- ARGV[3] = hash (Base64URL(SHA-256))

if (#KEYS ~= 2 or #ARGV ~= 3) then
    return redis.error_reply("save_session_script: wrong arity")
end

local mainKey = KEYS[1]
local setKey = KEYS[2]

local payload = ARGV[1]
local ttl = tonumber(ARGV[2]) or 0
local hash = ARGV[3]

ttl = math.floor(ttl)
if (ttl <= 0) then
    return redis.error_reply("save_session_script: invalid ttl")
end

redis.call('SET', mainKey, payload, 'EX', ttl)
redis.call('SADD', setKey, hash)

local curTtl = redis.call('TTL', setKey)
if (curTtl == -1 or curTtl < ttl) then
    redis.call('EXPIRE', setKey, ttl)
end

return 1