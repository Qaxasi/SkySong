
-- KEYS[1] = oldToken
-- KEYS[2] = newToken
-- ARGV[1] = json payload (String)
-- ARGV[2] = ttlSeconds (String)

local oldToken = KEYS[1]
local newToken = KEYS[2]
local payload = ARGV[1]
local ttl = tonumber(ARGV[2])

if redis.call('EXISTS', oldToken) == 0 then
    return 0
end

redis.call('SET', newToken, payload, 'EX', ttl)

redis.call('DEL', oldToken)

    return 1
