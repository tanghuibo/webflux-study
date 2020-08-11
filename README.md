# webflux study

## client

client 有两个接口，一个 `getTest` 和 `postTest`。

### getTest

method: get

|  |                        |
| ------ | ------------------------- |
| method | get                       |
| 参数   | key: 任意字符串           |
| header | sign: MD5(参数.key)       |
| 返回值 | checkResult: 签名是否正确 |

### postTest

|  |                        |
| ------ | ------------------------- |
| method | post                       |
| 参数   | 任意字符串           |
| header | sign: MD5(参数)      |
| 返回值 | checkResult: 签名是否正确 |
