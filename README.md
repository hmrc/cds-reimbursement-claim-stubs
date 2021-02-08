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

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
