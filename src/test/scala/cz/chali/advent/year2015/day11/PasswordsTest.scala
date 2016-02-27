package cz.chali.advent.year2015.day11

import org.scalatest.WordSpec

class PasswordsTest extends WordSpec {

    "Santa" should {

        "get this next password" in {
            assert(Passwords.generatePasswords("abcdefgz").head == "abcdffaa")
            assert(Passwords.generatePasswords("ghijklmn").head == "ghjaabcc")
        }

    }
}

