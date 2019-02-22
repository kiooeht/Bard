PROD?=../../../../../../productionAssets/cards
ORIG_IMAGES:=$(shell find $(PROD) -type f -name '*.png')
IMAGES:=$(patsubst $(PROD)/%.png, %.png, $(ORIG_IMAGES))
BIG_IMAGES:=$(patsubst $(PROD)/%.png, %_p.png, $(ORIG_IMAGES))

define mask_p
	convert $(1) "$(PROD)/../cardMasks/$(3).png" -alpha Off -compose CopyOpacity -composite $(2)
endef
define mask
	convert $(1) "$(PROD)/../cardMasks/$(3).png" -alpha Off -compose CopyOpacity -composite -resize 50% $(2)
endef

.PHONY: all verify clean

all: $(BIG_IMAGES) $(IMAGES)

attack/%_p.png: $(PROD)/attack/%.png
	@mkdir -p $(@D)
	$(call mask_p,$<,$@,attack)

skill/%_p.png: $(PROD)/skill/%.png
	@mkdir -p $(@D)
	$(call mask_p,$<,$@,skill)

power/%_p.png: $(PROD)/power/%.png
	@mkdir -p $(@D)
	$(call mask_p,$<,$@,power)

attack/%.png: $(PROD)/attack/%.png
	@mkdir -p $(@D)
	$(call mask,$<,$@,attack)

skill/%.png: $(PROD)/skill/%.png
	@mkdir -p $(@D)
	$(call mask,$<,$@,skill)

power/%.png: $(PROD)/power/%.png
	@mkdir -p $(@D)
	$(call mask,$<,$@,power)

verify:
	@echo $(ORIG_IMAGES)
	@echo $(IMAGES)
	@echo $(BIG_IMAGES)

clean:
	-@rm -r attack
	-@rm -r skill
	-@rm -r power
