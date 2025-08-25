
-- KEYS[1] = oldMainKey
-- KEYS[2] = newMainKey
-- KEYS[3] = tokensSetKey
-- ARGV[1] = json payload (String)
-- ARGV[2] = ttlSeconds (String/number)
-- ARGV[3] = oldToken (String)
-- ARGV[4] = newToken (String)

if (#KEYS ~= 3 or #ARGV ~= 4) then
    return redis.error_replay("rotateRefreshTokenScript: wrong arity")
end

local oldMainKey = KEYS[1]
local newMainKey = KEYS[2]
local tokensSet = KEYS[3]
local payload = ARGV[1]
local ttl = tonumber(ARGV[2])
local oldToken = ARGV[3]
local newToken = ARGV[4]

if redis.call('EXISTS', oldMainKey) == 0 then
    return 0
end

redis.call('SET', newMainKey, payload, 'EX', ttl)

local ok = pcall(function() redis.call('UNLINK', oldMainKey) end)
if not ok then redis.call('DEL', oldMainKey) end

if oldToken ~= newToken then
    redis.call('SREM', tokensSet, oldToken)
end
redis.call('SADD', tokensSet, newToken)

return 1


