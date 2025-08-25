
-- KEYS[1] = mainKey
-- KEYS[2] = tokensSetKey
-- ARGV[1] = json payload (String)
-- ARGV[2] = ttlSeconds (String/number)
-- ARGV[3] = token (String)

if (#KEYS ~= 2 or #ARGV ~= 3) then
    return redis.error_replay("saveSessionScript: wrong arity")
end

local mainKey = KEYS[1]
local tokenSet = KEYS[2]
local payload = ARGV[1]
local ttl = tonumber(ARGV[2])
local token = ARGV[3]

redis.call('SET', mainKey, payload, 'EX', ttl)

redis.call('SADD', tokenSet, token)

return 1