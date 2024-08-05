package dev.langchain4j.model.mistralai;

import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import org.junit.jupiter.api.Test;

import static dev.langchain4j.model.output.FinishReason.STOP;
import static org.assertj.core.api.Assertions.assertThat;

class MistralAiCompletionModelIT {

    LanguageModel codestral = MistralAiCompletionModel.withApiKey(System.getenv("MISTRAL_AI_API_KEY"));

    @Test
    void should_generate_code_completion_and_return_token_usage_and_finish_reason_stop() {
        // Given
        String codePrompt = "public static void main(String[] args) {";
        // When
        Response<String> response = codestral.generate(codePrompt);
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
    void should_generate_code_completion_with_suffix() {
        // Given
        MistralAiCompletionModel codestral = MistralAiCompletionModel.builder()
                .apiKey(System.getenv("MISTRAL_AI_API_KEY"))
                .modelName(MistralAiCodeModelName.CODESTRAL_LATEST)
                .logRequests(true)
                .logResponses(true)
                .build();

        String codePrompt = "public static void main(String[] args) {";
        String suffix = "int n = scanner.nextInt();\nSystem.out.println(fibonacci(n));";

        // When
        Response<String> response = codestral.generate(codePrompt, suffix);
        // Then
        System.out.println(
                codePrompt +
                response.content() +
                suffix); // print code completion
    }

}
