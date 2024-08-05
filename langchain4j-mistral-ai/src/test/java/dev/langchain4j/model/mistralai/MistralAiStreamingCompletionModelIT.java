package dev.langchain4j.model.mistralai;

import dev.langchain4j.model.chat.TestStreamingResponseHandler;
import dev.langchain4j.model.language.StreamingLanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import org.junit.jupiter.api.Test;

import static dev.langchain4j.model.output.FinishReason.STOP;
import static org.assertj.core.api.Assertions.assertThat;

class MistralAiStreamingCompletionModelIT {

    StreamingLanguageModel codestralStream = MistralAiStreamingCompletionModel.withApiKey(System.getenv("MISTRAL_AI_API_KEY"));

    @Test
    void should_stream_code_completion_and_return_token_usage_and_finish_reason_length() {
        // Given
        String codePrompt = "public static void main(String[] args) {";

        // When
        TestStreamingResponseHandler<String> handler = new TestStreamingResponseHandler<>();
        codestralStream.generate(codePrompt, handler);

        Response<String> response = handler.get();

        // Then
        System.out.println(codePrompt +
                response.content()); // print code completion

        TokenUsage tokenUsage = response.tokenUsage();
        assertThat(tokenUsage.inputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.outputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.totalTokenCount())
                .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());

        assertThat(response.finishReason()).isEqualTo(STOP);
    }

    @Test
    void should_generate_code_stream_completion_with_suffix() {
        // Given
        MistralAiStreamingCompletionModel codestral = MistralAiStreamingCompletionModel.builder()
                .apiKey(System.getenv("MISTRAL_AI_API_KEY"))
                .modelName(MistralAiCodeModelName.CODESTRAL_LATEST)
                .logRequests(true)
                .build();

        String codePrompt = "public static void main(String[] args) {";
        String suffix = "int n = scanner.nextInt();\nSystem.out.println(fibonacci(n));";

        // When
        TestStreamingResponseHandler<String> handler = new TestStreamingResponseHandler<>();
        codestral.generate(codePrompt, suffix, handler);

        Response<String> response = handler.get();

        // Then
        System.out.println(
                codePrompt +
                response.content() +
                suffix); // print code completion
    }
}
