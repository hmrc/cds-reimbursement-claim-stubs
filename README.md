# cds-reimbursement-claim-stubs

# Test Data

## Simulate User Journey

| GG EORI           | MRN                | Importer EORI     | Declarant EORI    | Declaration Response                  | Claim Submission Response                               |
|-------------------|--------------------|-------------------|-------------------|---------------------------------------|---------------------------------------------------------|
| AA12345678901234Z | 10ABCDEFGHIJKLMNO0 | GB123456789012345 | GB123456789012345 | Eori numbers match - full response    | Successful submission                                   |
| AA12345678901234Y | 10ABCDEFGHIJKLMNO1 |                   |                   | Eori numbers match - partial response | Successful submission                                   |
| AB12345678901234A | 20ABCDEFGHIJKLMNO1 |                   |                   | Eori numbers match - partial response | Successful submission but error response inside payload |
| AE12345678901234Z | 90ABCDEFGHIJKLMNO0 | GB123456789012345 | GB123456789012345 | Eori numbers match - full response    | Failed submission                                       |
| AC12345678901234Z | 30ABCDEFGHIJKLMNO0 |                   |                   | Eori not present                      | Successful submission                                   |
| AD12345678901234A | 40ABCDEFGHIJKLMNO1 |                   |                   | Eori present but no match             | Successful submission                                   |
| GB000000000000002 | 10AAAAAAAAAAAAAAA3 | GB000000000000002 | GB000000000000002 | Eori numbers match - Northern Ireland | Successful submission                                   |

# TPI05 API Simulated Responses

| EORI               | Http Response Code   | Status                                                 | 
|--------------------|----------------------|--------------------------------------------------------|
| AB12345678901234Z  | 200                  | 200 OK                                                 |
| AC12345678901234Z  | 200                  | 200 OK Returned but error returned from TPI05          |
| BB12345678901234Y  | 400                  | Bad request: missing mandatory fields in request       |
| CB12345678901234X  | 400                  | Bad request: payload does not validate against schema  |
| DB12345678901234W  | 401                  | Bad request: bearer token missing in HTTP header       |
| EB12345678901234V  | 403                  | WAF error                                              |
| FB12345678901234U  | 405                  | HTTP method not allowed                                |
| GB12345678901234T  | 500                  | Time outs, server is down, eis system errors etc       |
| 10AAAAAAAAAAAAAAA3 | 200                  | 200 OK                                                 |

# ACC14 API Simulated Responses

| MRN                | Http Response Code  | Status                                                  |
|--------------------|---------------------|---------------------------------------------------------|
| 21ABCDEFGHIJKLMNO0 | 200                 | Success - minimum response (no declaration information) |
| 22ABCDEFGHIJKLMNO1 | 200                 | Success - full response                                 |
| 41ABCDEFGHIJKLMNO1 | 400                 | Bad request - missing declaration                       |
| 41ABCDEFGHIJKLMNO2 | 400                 | Bad request - no security deposit                       |
| 40ABCDEFGHIJKLMNO3 | 405                 | Request made with incorrect HTTP method                 |
| 50ABCDEFGHIJKLMNO1 | 500                 | Time outs, server is down, eis system errors etc        |
| 10AAAAAAAAAAAAAAA3 | 200                 | Success - full response with Northern Ireland tax codes |


# Bank Account Reputation Responses
Any sort code which is not 6 digits long will be rejected with http 400 (BadRequest)
Any account number which is not 8 digits long will be rejected with http 400 (BadRequest)

In the following examples the sort code does not matter (as long as it's 6 digits)

About the fields below and their possible values [click here](https://github.com/hmrc/bank-account-reputation/blob/master/docs/assess/v3/assess.md#accountnumberwithsortcodeisvalid)

| Account Number  | accountNumberWithSortCodeIsValid | accountExists       |
|-----------------|----------------------------------|---------------------|
| 11001001        | Yes                              | Some(Yes)           |
| 11001002        | Yes                              | Some(Indeterminate) |
| 11001003        | Yes                              | Some(Error)         |
| 11001004        | Yes                              | Some(No)            |
| 11002001        | Indeterminate                    | Some(Yes)           |
| 11002002        | Indeterminate                    | Some(Indeterminate) |
| 11002003        | Indeterminate                    | Some(Error)         |
| 11002004        | Indeterminate                    | Some(No)            |
| 11003001        | Error                            | Some(Yes)           |
| 11003002        | Error                            | Some(Indeterminate) |
| 11003003        | Error                            | Some(Error)         |
| 11003004        | Error                            | Some(No)            |
| 11004004        | No                               | Some(No)            |
| 11004009        | No                               | None                |
| 11004009        | No                               | None                |
| Anything Else   | Yes                              | Some(Yes)           |

# BARS Simulated Responses

| Account Number | Http Response Code | Status              |
|----------------|--------------------|---------------------|
| 90909090       | 503                | SERVICE_UNAVAILABLE |
| 90909091       | 400                | BAD_REQUEST         |
| Anything else  | 200                | OK                  |

# View and Upload

## TPI01: Get Reimbursement Claims
| EORI number      | Http Response Code | Returned information                                     |
|------------------|--------------------|----------------------------------------------------------|
| GB744638982001   | 200                | Success response including multiple reimbursement claims |
| GB744638982002   | 200                | Success response including no reimbursement claims       |
| GB744638982008   | 200                | Success response including one of each TPI01 Case Status |
| TPI01_EORI_ERROR | 200                | Error response invalid EORI                              |
| TPI01_MISSING    | 400                | Error response BadRequest for fields missing             |
| TPI01_PATTERN    | 400                | Error response BadRequest for pattern error              |
| TPI01_500        | 500                | Error response InternalServerError for system timeout    |

### For testing a specific case status use the following

#### Overpayments or rejected goods
| EORI           | TPI01 Case Status                 | Frontend Status / SubStatus |
|----------------|-----------------------------------|-----------------------------|
| GB00000000001  | Open                              | In Progress                 |
| GB00000000002  | Open-Analysis                     | In Progress                 |
| GB00000000003  | Pending-Approval                  | In Progress                 |
| GB00000000004  | Pending-Queried                   | Pending                     |
| GB00000000005  | Resolved-Withdrawn                | Closed / Withdrawn          |
| GB00000000006  | Rejected-Failed Validation        | Closed / Failed Validation  |
| GB00000000007  | Resolved-Rejected                 | Closed / Rejected           |
| GB00000000008  | Open-Rework                       | In Progress                 |
| GB00000000009  | Paused                            | In Progress                 |
| GB000000000010 | Resolved-No Reply                 | Closed / No reply           |
| GB000000000011 | RTBH-Sent                         | Pending                     |
| GB000000000012 | Resolved-Refused                  | Closed / Refused            |
| GB000000000013 | Pending Payment Confirmation      | In Progress                 |
| GB000000000014 | Resolved-Approved                 | Closed / Approved           |
| GB000000000015 | Resolved-Partial Refused          | Closed / Refused            |
| GB000000000016 | Pending Decision Letter           | In Progress                 |
| GB000000000017 | Approved                          | In Progress                 |
| GB000000000018 | Analysis Rework                   | In Progress                 |
| GB000000000019 | Rework-Payment Details            | In Progress                 |
| GB000000000020 | Pending-RTBH                      | In Progress                 |
| GB000000000021 | Reply To RTBH                     | Pending                     |
| GB000000000022 | Pending-Compliance Recommendation | In Progress                 |
| GB000000000023 | Pending-Compliance Check Query    | Pending                     |
| GB000000000024 | Pending-Compliance Check          | In Progress                 |

### Security or guarantee
| EORI           | TPI01 Case Status                 | Frontend Status / SubStatus |
|----------------|-----------------------------------|-----------------------------|
| GB10000000001  | Open                              | In Progress                 |
| GB10000000002  | Pending-Approval                  | Pending                     |
| GB10000000003  | Pending-Payment                   | Pending                     |
| GB10000000004  | Partial Refund                    | Pending                     |
| GB10000000005  | Resolved-Refund                   | Closed / Approved           |
| GB10000000006  | Pending-Query                     | Pending                     |
| GB10000000007  | Resolved-Manual BTA               | Closed /Refused             |
| GB10000000008  | Pending-C18                       | Pending                     |
| GB10000000009  | Closed-C18 Raised                 | Closed / Underpayment       |
| GB100000000010 | RTBH Letter Initiated             | Pending                     |
| GB100000000011 | Awaiting RTBH Letter Response     | Pending                     |
| GB100000000012 | Reminder Letter Initiated         | Pending                     |
| GB100000000013 | Awaiting Reminder Letter Response | Pending                     |
| GB100000000014 | Decision Letter Initiated         | Pending                     |
| GB100000000015 | Partial BTA                       | Pending                     |
| GB100000000016 | Partial BTA/Refund                | Pending                     |
| GB100000000017 | Resolved-Auto BTA                 | Closed / Refused            |
| GB100000000018 | Resolved-Manual BTA/Refund        | Closed / Part approved      |
| GB100000000019 | Open-Extension Granted            | In Progress                 |
| GB100000000020 | Resolved-Withdrawn                | Closed / Withdrawn          |

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").


[]: https://github.com/hmrc/bank-account-reputation/blob/master/docs/assess/v3/assess.md#accountnumberwithsortcodeisvalid