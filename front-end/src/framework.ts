interface FrameworkState {
    name: string;
    footer: string;
    dataPlugins: Plugin[];
    visualPlugins: Plugin[];
    graphJson: string;
    processDone: boolean;
}

interface Plugin {
    name: string;
    type: string;
}

export type {FrameworkState, Plugin}
