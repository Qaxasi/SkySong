
-- KEYS[1] = key
-- ARGV[1] = payload json
-- ARGV[2] = ttlSeconds


local RESPONSE_OK = 1

if (#KEYS ~= 1 or #ARGV ~= 2) then
    return redis.error_reply("session.save - wrong arity")
end

local key = KEYS[1]
local payload = ARGV[1]
local ttl = tonumber(ARGV[2]) or 0

ttl = math.floor(ttl)
if ttl <= 0 then
    return redis.error_reply("session save - bad ttl")
end

redis.call('SET', key, payload, 'EX', ttl)

return RESPONSE_OK