
-- KEYS[1] = setKey
-- ARGV[1] = keyPrefix

if (#KEYS ~= 1 or #ARGV ~= 1) then
    return redis.error_reply("wrong arity")
end

local setKey = KEYS[1]
local prefix = ARGV[1]

local hashes = redis.call('SMEMBERS', setKey)

local deleted = 0

for _,h in ipairs(hashes) do
    local key = prefix .. h
    local res = redis.call('UNLINK', key)
    deleted = deleted + res
end

redis.call('UNLINK', setKey)

return deleted
