/**
 * This file is part of Graylog.
 *
 * Graylog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graylog.plugins.views.search.searchtypes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import org.graylog.plugins.views.search.Filter;
import org.graylog.plugins.views.search.SearchType;
import org.graylog2.rest.models.messages.responses.ResultMessageSummary;

import javax.annotation.Nullable;
import java.util.List;

@AutoValue
@JsonTypeName(MessageList.NAME)
@JsonDeserialize(builder = AutoValue_MessageList.Builder.class)
public abstract class MessageList implements SearchType {
    public static final String NAME = "messages";

    @Override
    public abstract String type();

    @Override
    @Nullable
    @JsonProperty
    public abstract String id();

    @Nullable
    @Override
    public abstract Filter filter();

    @JsonProperty
    public abstract int limit();

    @JsonProperty
    public abstract int offset();

    @Nullable
    public abstract List<Sort> sort();

    public static Builder builder() {
        return new AutoValue_MessageList.Builder()
                .type(NAME)
                .limit(150)
                .offset(0);
    }

    public abstract Builder toBuilder();

    @Override
    public SearchType applyExecutionContext(ObjectMapper objectMapper, JsonNode state) {
        final boolean hasLimit = state.hasNonNull("limit");
        final boolean hasOffset = state.hasNonNull("offset");
        if (hasLimit || hasOffset) {
            final Builder builder = toBuilder();
            if (hasLimit) {
                builder.limit(state.path("limit").asInt());
            }
            if (hasOffset) {
                builder.offset(state.path("offset").asInt());
            }
            return builder.build();
        }
        return this;
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract Builder type(String type);

        @JsonProperty
        public abstract Builder id(@Nullable String id);

        @JsonProperty
        public abstract Builder filter(@Nullable Filter filter);

        @JsonProperty
        public abstract Builder limit(int limit);

        @JsonProperty
        public abstract Builder offset(int offset);

        @JsonProperty
        public abstract Builder sort(@Nullable List<Sort> sort);

        public abstract MessageList build();
    }

    @AutoValue
    public abstract static class Result implements SearchType.Result {

        @Override
        @JsonProperty
        public abstract String id();

        @Override
        @JsonProperty
        public String type() {
            return NAME;
        }

        @JsonProperty
        public abstract List<ResultMessageSummary> messages();

        @JsonProperty
        public abstract long totalResults();

        public static Builder builder() {
            return new AutoValue_MessageList_Result.Builder();
        }

        public static Builder result(String searchTypeId) {
            return builder().id(searchTypeId);
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder id(String id);

            public abstract Builder messages(List<ResultMessageSummary> messages);

            public abstract Builder totalResults(long totalResults);

            public abstract Result build();
        }
    }
}
