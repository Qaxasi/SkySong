
-- KEYS[1] = mainKey
-- KEYS[2] = tokensSetKey
-- ARGV[1] = json payload (String)
-- ARGV[2] = ttlSeconds (String/number)
-- ARGV[3] = token (String)

if (#KEYS ~= 2 or #ARGV ~= 3) then
    return redis.error_reply("saveSessionScript: wrong arity")
end

local mainKey = KEYS[1]
local tokenSet = KEYS[2]
local payload = ARGV[1]
local ttl = tonumber(ARGV[2])
local token = ARGV[3]

if (not ttl or ttl <= 0) then
    return redis.error_reply("saveSessionScript: invalid ttl")
end

redis.call('SET', mainKey, payload, 'EX', ttl)

redis.call('SADD', tokenSet, token)

local curTtl = redis.call('TTL', tokenSet)
if (curTtl < 0 or curTtl < ttl) then
    redis.call('EXPIRE', tokenSet, ttl)
end

return 1