
-- KEYS[1] = oldKey
-- KEYS[2] = newKey
-- ARGV[1] = payload - json
-- ARGV[2] = ttlSeconds

local RESPONSE_OK = 1
local RESPONSE_NOT_FOUND = 0
local RESPONSE_CONFLICT = 2

if (#KEYS ~= 2 or #ARGV ~= 2) then
    return redis.error_reply("session.rotate wrong arity")
end

local oldKey = KEYS[1]
local newKey = KEYS[2]
local payload = ARGV[1]
local ttl = tonumber(ARGV[2]) or 0

ttl = math.floor(ttl)
if ttl <= 0 then
    return redis.error_reply("session.rotate bad ttl")
end

if oldKey == newKey then
    return RESPONSE_CONFLICT
end


if redis.call('EXISTS', oldKey) == 0 then
    return RESPONSE_NOT_FOUND
end

if not redis.call('SET', newKey, payload, 'EX', ttl, "NX") then
    return RESPONSE_CONFLICT
end

redis.call('DEL', oldKey)

return RESPONSE_OK

