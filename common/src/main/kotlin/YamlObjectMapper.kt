import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule

class YamlObjectMapper {
    companion object {
        val instance = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
    }
}