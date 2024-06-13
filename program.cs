Demo integration with confluent cloud using this https://github.com/LGouellec/kafka-streams-dotnet library.

using System;
using Streamiz.Kafka.Net;
using Streamiz.Kafka.Net.Metrics;
using Streamiz.Kafka.Net.SerDes;
using Streamiz.Kafka.Net.Stream;
using Streamiz.Kafka.Net.Table;
using Confluent.Kafka;
using Microsoft.Extensions.Options; 


namespace sample_stream_demo
{
    internal class program
    {
        static async Task Main(string[] args)
        {
            // Stream configuration
            var config = new StreamConfig<StringSerDes, StringSerDes>();
            config.ApplicationId = "test-app";
            config.BootstrapServers = "";
            config.SecurityProtocol=SecurityProtocol.SaslSsl;
            config.SaslMechanism=SaslMechanism.Plain;
            config.SaslUsername="";
            config.SaslPassword="";
            StreamBuilder builder = new StreamBuilder();

            // Stream "test" topic with filterNot condition and persist in "test-output" topic.
            builder.Stream<string, string>("thetopic")
                .To("test-output");

            // Create a table with "test-ktable" topic, and materialize this with in memory store named "test-store"
         //   builder.Table("test-ktable", InMemory<string, string>.As("test-store"));

            // Build topology
            Topology t = builder.Build();

            // Create a stream instance with toology and configuration
            KafkaStream stream = new KafkaStream(t, config);

            // Start stream instance with cancellable token
            await stream.StartAsync();

            await Task.Delay(10000000);

            stream.Dispose();
        }
    }
}
