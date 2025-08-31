
-- KEYS[1] = tokenSetKey (user set key)
-- KEYS[2] = sessionKeyPrefix (user session key prefix)

if (#KEYS ~= 2) then
    return redis.error_reply("delete_user_session script: wrong arity")
end

local tokenSetKey = KEYS[1]
local sessionKeyPrefix = KEYS[2]

local tokens = redis.call('SMEMBERS', KEYS[1])
for _, token in ipairs(tokens) do
    redis.call('UNLINK', KEYS[2] .. token)
end

redis.call('UNLINK', KEYS[1])
return #tokens