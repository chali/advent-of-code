package cz.chali.advent.year2015.day12

import cz.chali.advent.input.InputReader
import org.json4s._
import org.json4s.jackson.JsonMethods._

object JsonDocumentTraverse {

    def traverseAndSum(rawData: String): Int = {
        val parsedDocument = parse(rawData, useBigDecimalForDouble = false, useBigIntForLong = false)
        processValue(parsedDocument)
    }

    def processValue(value: JValue): Int = value match {
        case nestedObject: JObject => processObject(nestedObject.obj)
        case array: JArray => processArray(array.arr)
        case number: JInt => number.num.toInt
        case _ => 0
    }

    def processObject(objectFields: List[(String, JValue)]): Int = {
        val values =  objectFields.map(_._2)
        if (! containsRed(values))
            processArray(values)
        else
            0
    }

    def containsRed(values: List[JValue]): Boolean = {
        values.exists {
            case string: JString => string.values == "red"
            case _ => false
        }
    }

    def processArray(values: List[JValue]): Int = {
        values.map(processValue).sum
    }

    def main(args: Array[String]) {
        val data: String = InputReader.readText("/cz/chali/advent/year2015/day12/data.json")
        val sum: Int = traverseAndSum(data)
        println(s"Sum of all number in document is: $sum")
    }
}
