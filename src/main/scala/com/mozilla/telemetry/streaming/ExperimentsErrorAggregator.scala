// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
package com.mozilla.telemetry.streaming

import java.sql.{Date, Timestamp}
import com.mozilla.telemetry.timeseries.SchemaBuilder

object ExperimentsErrorAggregator {
  val outputPrefix = "experiment_error_aggregates/v1"
  val queryName = "experiment_error_aggregates"

  private val countHistogramErrorsSchema = (new SchemaBuilder()).build
  private val thresholdHistograms: Map[String, (List[String], List[Int])] = Map.empty

  private val dimensionsSchema = new SchemaBuilder()
    .add[Timestamp]("timestamp")  // Windowed
    .add[Date]("submission_date")
    .add[String]("channel")
    .add[String]("version")
    .add[String]("os_name")
    .add[String]("country")
    .add[String]("experiment_id")
    .add[String]("experiment_branch")
    .build

  private val metricsSchema = new SchemaBuilder()
    .add[Float]("usage_hours")
    .add[Int]("count")
    .add[Int]("subsession_count")
    .add[Int]("main_crashes")
    .add[Int]("content_crashes")
    .add[Int]("gpu_crashes")
    .add[Int]("plugin_crashes")
    .add[Int]("gmplugin_crashes")
    .add[Int]("content_shutdown_crashes")
    .build

  def prepare: Unit = {
    ErrorAggregator.setPrefix(outputPrefix)
    ErrorAggregator.setQueryName(queryName)
    ErrorAggregator.setSchemas(metricsSchema, dimensionsSchema, thresholdHistograms, countHistogramErrorsSchema)
  }

  def main(args: Array[String]): Unit = {
    ExperimentsErrorAggregator.prepare
    ErrorAggregator.run(args)
  }
}
