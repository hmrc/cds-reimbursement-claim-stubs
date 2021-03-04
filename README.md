# cds-reimbursement-claim-stubs

# Test Data

## Simulate User Journey

|MRN|EORI|Declaration Response| Claim Submission Response
|---|---|---|---|
|10ABCDEFGHIJKLMNO0|AA12345678901234Z| Eori numbers match - full response  | Successful submission | 
|10ABCDEFGHIJKLMNO1|AA12345678901234Y| Eori numbers match - partial response  | Successful submission | 
|20ABCDEFGHIJKLMNO1|AB12345678901234A| Eori numbers match - partial response | Successful submission but error response inside payload |
|90ABCDEFGHIJKLMNO0|AE12345678901234Z| Eori numbers match - full response  | Failed submission | 
|30ABCDEFGHIJKLMNO0|AC12345678901234Z| Eori not present  | Successful submission |
|40ABCDEFGHIJKLMNO1|AD12345678901234A| Eori present but no match  | Successful submission |

# TPI05 API Simulated Responses

|EORI|Http Response Code| Status
|---|---|---|
|AB12345678901234Z|200| 200 OK  |
|AC12345678901234Z|200| 200 OK Returned but error returned from TPI05 |
|BB12345678901234Y|400| Bad request: missing mandatory fields in request|
|CB12345678901234X|400| Bad request: payload does not validate against schema |
|DB12345678901234W|401| Bad request: bearer token missing in HTTP header|
|EB12345678901234V|403| WAF error|
|FB12345678901234U|405| HTTP method not allowed|
|GB12345678901234T|500| Time outs, server is down, eis system errors etc|

# ACC14 API Simulated Responses

|MRN|Http Response Code| Status |
|---|---|---|
|21ABCDEFGHIJKLMNO0|200| Success - minimum response (no declaration information) 
|22ABCDEFGHIJKLMNO1|200| Success - full response |
|41ABCDEFGHIJKLMNO1|400| Bad request - missing declaration |
|41ABCDEFGHIJKLMNO2|400| Bad request - no security deposit |
|40ABCDEFGHIJKLMNO3|405| Request made with incorrect HTTP method |
|50ABCDEFGHIJKLMNO1|500| Time outs, server is down, eis system errors etc  |


# Bank Account Reputation Responses
Any sort code which is not 6 digits long will be rejected with http 400 (BadRequest)
Any account number which is not 8 digits long will be rejected with http 400 (BadRequest)

In the following examples the sort code does not matter (as long as it's 6 digits)

About the fields below and their possible values [click here](https://github.com/hmrc/bank-account-reputation/blob/master/docs/assess/v3/assess.md#accountnumberwithsortcodeisvalid)

|Account Number| accountNumberWithSortCodeIsValid | accountExists |
|---|---|---|
|11001001|Yes | Some(Yes)|
|11001002|Yes | Some(Indeterminate)|
|11001003|Yes | Some(Error)|
|11001004|Yes | Some(No)|
|11002001|Indeterminate | Some(Yes)|
|11002002|Indeterminate | Some(Indeterminate)|
|11002003|Indeterminate | Some(Error)|
|11002004|Indeterminate | Some(No)|
|11003001|Error | Some(Yes)|
|11003002|Error | Some(Indeterminate)|
|11003003|Error | Some(Error)|
|11003004|Error | Some(No)|
|11004004|No | Some(No)|
|11004009|No | None|
|Anything Else|Yes|Some(Yes)|



### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").


[]: https://github.com/hmrc/bank-account-reputation/blob/master/docs/assess/v3/assess.md#accountnumberwithsortcodeisvalid