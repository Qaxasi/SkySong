
-- KEYS[1] = mainKey
-- KEYS[2] = tokensSetKey
-- ARGV[1] = json payload
-- ARGV[2] = ttlSeconds
-- ARGV[3] = token

if (#KEYS ~= 2 or #ARGV ~= 3) then
    return redis.error_reply("save_session_script: wrong arity")
end

local mainKey = KEYS[1]
local tokenSetKey = KEYS[2]

local payload = ARGV[1]
local ttl = tonumber(ARGV[2])
local token = ARGV[3]

if (not ttl or ttl <= 0) then
    return redis.error_reply("save_session_script: invalid ttl")
end

redis.call('SET', mainKey, payload, 'EX', ttl)
redis.call('SADD', tokenSetKey, token)

local curTtl = redis.call('TTL', tokenSetKey)
if (curTtl < 0 or curTtl < ttl) then
    redis.call('EXPIRE', tokenSetKey, ttl)
end

return 1