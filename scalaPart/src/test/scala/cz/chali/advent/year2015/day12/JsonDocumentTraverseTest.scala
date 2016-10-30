package cz.chali.advent.year2015.day12

import org.scalatest.WordSpec

class JsonDocumentTraverseTest extends WordSpec {

    "Elfs" should {

        "count those sums of numbers" in {
            assert(JsonDocumentTraverse.traverseAndSum("[1,2,3]") == 6)
            assert(JsonDocumentTraverse.traverseAndSum("{\"a\":2,\"b\":4}") == 6)
            assert(JsonDocumentTraverse.traverseAndSum("[[[3]]]") == 3)
            assert(JsonDocumentTraverse.traverseAndSum("{\"a\":{\"b\":4},\"c\":-1}") == 3)
            assert(JsonDocumentTraverse.traverseAndSum("{\"a\":[-1,1]}") == 0)
            assert(JsonDocumentTraverse.traverseAndSum("[-1,{\"a\":1}]") == 0)
            assert(JsonDocumentTraverse.traverseAndSum("[]") == 0)
            assert(JsonDocumentTraverse.traverseAndSum("{}") == 0)
        }

    }
}
