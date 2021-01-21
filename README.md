
# cds-reimbursement-claim-stubs

Running on port 7502

#### TPI05 Submit Reimbursement Claim

See **CDSProfile.scala**

|EORI|Http Response Code|
|---|---|
|AB12345678901234Z|200|
|BB12345678901234Y|400| 
|CB12345678901234X|400|
|DB12345678901234W|401|
|EB12345678901234V|403|
|FB12345678901234U|405|
|GB12345678901234T|500|

#### ACC14 Request Declaration Information

See **DeclarationInfoRequestController.scala**

|MRN|Http Response Code|
|---|---|
|57WAFResponse11111|403|
|57TimeoutResponse1|500|
|57CouldNotProcess1|400|
|57NoDeclarationF11|400|
|57NoSecurityDepos1|400|
|57EmptyResponse111|200|
|57MinimumResponse1|200|
|Anything else|200|



### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
