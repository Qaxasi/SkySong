
-- KEYS[1] = tokenSetKey (user set key)
-- KEYS[2] = mainKeyPrefix (user main key prefix)

if (#KEYS ~= 2) then
    return redis.error_reply("delete_user_session script: wrong arity")
end

local tokenSetKey = KEYS[1]
local mainKeyPrefix = KEYS[2]

local tokens = redis.call('SMEMBERS', tokenSetKey)
for _, token in ipairs(tokens) do
    redis.call('UNLINK', mainKeyPrefix .. token)
end

redis.call('UNLINK', tokenSetKey)
return #tokens