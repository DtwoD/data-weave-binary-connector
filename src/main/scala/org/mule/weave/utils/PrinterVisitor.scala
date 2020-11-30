package org.mule.weave.utils

import java.util.{Map => JMap}

import org.mule.runtime.weave.dwb.api.{IWeaveValue, IWeaveValueVisitor}
import org.mule.runtime.weave.dwb.api.values._

class PrinterVisitor() extends IWeaveValueVisitor {
  val builder: StringBuilder = new StringBuilder()

  override def toString: String = {
    builder.toString()
  }

  override def visitString(value: IWeaveStringValue): Unit = {
    builder.append(value.toString)
  }

  override def visitObject(value: IWeaveObjectValue): Unit = {
    val map = value.evaluate
    val entrySet = map.entrySet()
    builder.append("{")
    var first = true
    entrySet.forEach((entry: JMap.Entry[String, IWeaveValue[_]]) => {
      if (first) {
        first = false
      } else {
        builder.append(", ")
      }
      val key = entry.getKey
      val value = entry.getValue
      builder.append(key) //TODO: change this into a KeyWeaveValue to support namespaces and attributes
      builder.append(": ")
      value.accept(this)
    })
    builder.append("}")
  }

  override def visitArray(value: IWeaveArrayValue): Unit = {
    val list = value.evaluate
    builder.append("[")
    var first = true
    list.forEach(item => {
      if (first) {
        first = false
      } else {
        builder.append(", ")
      }
      item.accept(this)
    })
    builder.append("]")

  }

  override def visitInt(value: IWeaveIntValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitLong(value: IWeaveLongValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitDouble(value: IWeaveDoubleValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitBigInt(value: IWeaveBigIntValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitBigDecimal(value: IWeaveBigDecimalValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitBinary(value: IWeaveBinaryValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitBoolean(value: IWeaveBooleanValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitNull(value: IWeaveNullValue): Unit = {
    builder.append("null")
  }

  override def visitDateTime(value: IWeaveDateTimeValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitLocalDateTime(value: IWeaveLocalDateTimeValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitTime(value: IWeaveTimeValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitLocalTime(value: IWeaveLocalTimeValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitTimeZone(value: IWeaveTimeZoneValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitLocalDate(value: IWeaveLocalDateValue): Unit = {
    builder.append(value.evaluate)
  }

  override def visitPeriod(value: IWeavePeriodValue): Unit = {
    builder.append(value.evaluate)
  }
}
