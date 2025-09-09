
-- KEYS[1] = tokenSetKey
-- KEYS[2] = mainKeyPrefix

if (#KEYS ~= 2) then
    return redis.error_reply("delete_user_sessions: wrong arity")
end

local tokenSetKey = KEYS[1]
local mainKeyPrefix = KEYS[2]

local tokens = redis.call('SMEMBERS', tokenSetKey)
for _, token in ipairs(tokens) do
    redis.call('UNLINK', mainKeyPrefix .. token)
end

redis.call('UNLINK', tokenSetKey)
return #tokens