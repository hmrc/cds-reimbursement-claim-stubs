package uk.gov.hmrc.cdsreimbursementclaimstubs.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import DeclarationInfoHelpers._

class DeclarationInfoResponsesSpec extends AnyWordSpec with Matchers {

  "MRN validation" should {
    "be valid" in {
      val mrns = List("57WAFResponse11111", "57TimeoutResponse1", "57CouldNotProcess1", "57NoDeclarationF11", "57NoSecurityDepos1", "57EmptyResponse111", "57MinimumResponse1")
      mrns.foreach(isMRNValid(_) shouldBe true)

    }

    "fail if first 2 is not a number" in {
      isMRNValid("aaWAFResponse11111") shouldBe false
    }

    "fail if chars 3-4 are numbers" in {
      isMRNValid("1122FResponse11111") shouldBe false
    }

    "fail if the last char is a letter" in {
      isMRNValid("11GBFResponse1111a") shouldBe false
    }

    "fail if not 18 characters" in {
      isMRNValid("11GBResponse11111") shouldBe false
    }


  }
}
