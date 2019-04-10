PROD?=../productionAssets/cards
PORTRAITS?=../src/main/resources/bardAssets/images/1024Portraits
TARGET?=../target/cards

ORIG_IMAGES:=$(shell find $(PROD) -type f -name '*.png')
IMAGES:=$(patsubst $(PROD)/%.png, $(TARGET)/%.png, $(ORIG_IMAGES))
BIG_IMAGES:=$(patsubst $(PROD)/%.png, $(PORTRAITS)/%.png, $(ORIG_IMAGES))

define mask_p
	convert $(1) "$(PROD)/../cardMasks/$(3).png" -alpha Off -compose CopyOpacity -composite $(2)
endef
define mask
	convert $(1) "$(PROD)/../cardMasks/$(3).png" -alpha Off -compose CopyOpacity -composite -resize 50% $(2)
endef

.PHONY: all verify clean

all: $(BIG_IMAGES) $(IMAGES)

$(PORTRAITS)/attack/%.png: $(PROD)/attack/%.png
	@mkdir -p $(@D)
	$(call mask_p,$<,$@,attack)

$(PORTRAITS)/skill/%.png: $(PROD)/skill/%.png
	@mkdir -p $(@D)
	$(call mask_p,$<,$@,skill)

$(PORTRAITS)/power/%.png: $(PROD)/power/%.png
	@mkdir -p $(@D)
	$(call mask_p,$<,$@,power)

$(TARGET)/attack/%.png: $(PROD)/attack/%.png
	@mkdir -p $(@D)
	$(call mask,$<,$@,attack)

$(TARGET)/skill/%.png: $(PROD)/skill/%.png
	@mkdir -p $(@D)
	$(call mask,$<,$@,skill)

$(TARGET)/power/%.png: $(PROD)/power/%.png
	@mkdir -p $(@D)
	$(call mask,$<,$@,power)

verify:
	@echo $(ORIG_IMAGES)
	@echo $(IMAGES)
	@echo $(BIG_IMAGES)

clean:
	-@rm -r $(PORTRAITS)
	#-@rm -r $(TARGET) target is cleaned by maven before this
